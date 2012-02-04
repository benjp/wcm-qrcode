package org.gatein.portal.people;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.MembershipTypeHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.juzu.Action;
import org.juzu.Resource;
import org.juzu.View;
import org.juzu.Path;
import org.juzu.template.Template;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class Controller
{

   /** . */
   @Inject
   @Path("index.gtmpl")
   Template indexTemplate;

   /** . */
   @Inject
   @Path("users.gtmpl")
   Template usersTemplate;

   /** . */
   @Inject
   @Path("groups.gtmpl")
   Template groupsTemplate;

   /** . */
   private final OrganizationService orgService;

   /** . */
   private final UserHandler userHandler;

   /** . */
   private final GroupHandler groupHandler;

   /** . */
   private final MembershipHandler membershipHandler;

   /** . */
   private final MembershipTypeHandler membershipTypeHandler;

   @Inject
   public Controller(OrganizationService orgService)
   {
      this.orgService = orgService;
      this.userHandler = orgService.getUserHandler();
      this.groupHandler = orgService.getGroupHandler();
      this.membershipHandler = orgService.getMembershipHandler();
      this.membershipTypeHandler = orgService.getMembershipTypeHandler();
   }

   @View
   public void index() throws IOException
   {
      indexTemplate.render();
   }
   
   @Resource
   public void findUsers(String userName) throws Exception
   {
      if (userName != null)
      {
         userName = userName.trim();
      }
      Query query = new Query();
      if (userName != null && userName.length() > 0)
      {
         query.setUserName("*" + userName + "*");
      }
      ListAccess<User> list = userHandler.findUsersByQuery(query);
      int from = 0;
      int size = 10;
      int length = Math.min(from + size, list.getSize());
      usersTemplate.render(Collections.singletonMap("users", list.load(from, length)));
   }

   @Resource
   public void findGroups(String groupName, List<String> userName) throws Exception
   {
      if (groupName != null)
      {
         groupName = groupName.trim();
      }
      
      // All membership types
      LinkedHashSet<String> membershipTypes = new LinkedHashSet<String>();
      
      // All groups
      List<Group> groups = new ArrayList<Group>();

      // Map<GroupId>, Map<MembershipType, MembershipId>>
      Map<String, Map<String, List<String>>> toRemove = new HashMap<String, Map<String, List<String>>>();
      
      // Map<GroupId, Map<MembershipType, UserName>> 
      Map<String, Map<String, List<String>>> toAdd = new HashMap<String, Map<String, List<String>>>();
      
      // Compute the view
      for (MembershipType membershipType : (Collection<MembershipType>)membershipTypeHandler.findMembershipTypes())
      {
         membershipTypes.add(membershipType.getName());
      }
      for (Group group : (List<Group>)groupHandler.getAllGroups())
      {
         if (groupName == null || groupName.length() == 0 || group.getGroupName().contains(groupName))
         {
            groups.add(group);
            if (userName != null)
            {
               Map<String, List<String>> toRemoveByGroup = new HashMap<String, List<String>>();
               Map<String, List<String>> toAddByGroup = new HashMap<String, List<String>>();
               for (String _userName : userName)
               {
                  Collection<Membership> membershipsInGroup = (Collection<Membership>)membershipHandler.findMembershipsByUserAndGroup(_userName, group.getId());
                  Set<String> membershipsOutOfGroup = new HashSet<String>(membershipTypes);
                  for (Membership membership : membershipsInGroup)
                  {
                     List<String> toRemoveByGroupAndByMembership = toRemoveByGroup.get(membership.getMembershipType());
                     if (toRemoveByGroupAndByMembership == null)
                     {
                        toRemoveByGroup.put(membership.getMembershipType(), toRemoveByGroupAndByMembership = new ArrayList<String>());
                     }
                     toRemoveByGroupAndByMembership.add(membership.getId());
                     membershipsOutOfGroup.remove(membership.getMembershipType());
                  }
                  for (String membership : membershipsOutOfGroup)
                  {
                     List<String> toAddByGroupAndMembership = toAddByGroup.get(membership);
                     if (toAddByGroupAndMembership == null)
                     {
                        toAddByGroup.put(membership, toAddByGroupAndMembership = new ArrayList<String>());
                     }
                     toAddByGroupAndMembership.add(_userName);
                  }
               }
               toRemove.put(group.getId(), toRemoveByGroup);
               toAdd.put(group.getId(), toAddByGroup);
            }
         }
      }
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("groups", groups);
      params.put("toRemove", toRemove);
      params.put("toAdd", toAdd);
      params.put("membershipTypes", membershipTypes);
      groupsTemplate.render(params);
   }

   @Action
   public void removeMembership(List<String> id) throws Exception
   {
      if (id != null)
      {
         for (String _id : id)
         {
            membershipHandler.removeMembership(_id, true);
         }
      }
   }

   @Action
   public void addMembership(String type, String groupId, List<String> userName) throws Exception
   {
      if (userName != null)
      {
         Group group = groupHandler.findGroupById(groupId);
         MembershipType membershipType = membershipTypeHandler.findMembershipType(type);
         for (String _userName : userName)
         {
            User user = userHandler.findUserByName(_userName);
            membershipHandler.linkMembership(user, group, membershipType, true);
         }
      }
   }
}
