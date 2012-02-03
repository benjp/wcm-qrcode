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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
   public void findGroups(String groupName, String userName) throws Exception
   {
      if (groupName != null)
      {
         groupName = groupName.trim();
      }
      if (userName != null)
      {
         userName = userName.trim();
      }
      LinkedHashSet<String> membershipTypes = new LinkedHashSet<String>();
      for (MembershipType membershipType : (Collection<MembershipType>)membershipTypeHandler.findMembershipTypes())
      {
         membershipTypes.add(membershipType.getName());
      }
      List<Group> groups = new ArrayList<Group>();
      Map<String, Map<String, String>> memberships = new HashMap<String, Map<String, String>>();
      for (Group group : (List<Group>)groupHandler.getAllGroups())
      {
         if (groupName == null || groupName.length() == 0 || group.getGroupName().contains(groupName))
         {
            groups.add(group);
            if (userName != null && userName.length() > 0)
            {
               Map<String, String> groupMemberships = new HashMap<String, String>();
               for (Membership membership : (Collection<Membership>)membershipHandler.findMembershipsByUserAndGroup(userName, group.getId()))
               {
                  groupMemberships.put(membership.getMembershipType(), membership.getId());
               }
               memberships.put(group.getId(), groupMemberships);
            }
         }
      }
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("userName", userName);
      params.put("groups", groups);
      params.put("memberships", memberships);
      params.put("membershipTypes", membershipTypes);
      groupsTemplate.render(params);
   }

   @Action
   public void removeMembership(String id) throws Exception
   {
      membershipHandler.removeMembership(id, true);
   }

   @Action
   public void addMembership(String type, String groupId, String userName) throws Exception
   {
      Group group = groupHandler.findGroupById(groupId);
      User user = userHandler.findUserByName(userName);
      MembershipType membershipType = membershipTypeHandler.findMembershipType(type);
      membershipHandler.linkMembership(user, group, membershipType, true);
   }
}
