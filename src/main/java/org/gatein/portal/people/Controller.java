package org.gatein.portal.people;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
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

   @Inject
   public Controller(OrganizationService orgService)
   {
      this.orgService = orgService;
      this.userHandler = orgService.getUserHandler();
      this.groupHandler = orgService.getGroupHandler();
      this.membershipHandler = orgService.getMembershipHandler();
   }

   @View
   public void index() throws IOException
   {
      indexTemplate.render();
   }
   
   @Resource
   public void users(String userName) throws Exception
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
   public void groups(String groupName, String userName) throws Exception
   {
      if (groupName != null)
      {
         groupName = groupName.trim();
      }
      if (userName != null)
      {
         userName = userName.trim();
      }
      @SuppressWarnings("unchecked")
      List<Group> list = (List<Group>)groupHandler.getAllGroups();
      List<Group> groups = new ArrayList<Group>(list.size());
      for (Group group : list)
      {
         if (groupName == null || groupName.length() == 0 || group.getGroupName().contains(groupName))
         {
            groups.add(group);
         }
      }
      Map<String, List<String>> memberships = Collections.emptyMap();
      if (userName != null && userName.length() > 0)
      {
         memberships = new HashMap<String, List<String>>();
         Collection<Membership> membershipList = membershipHandler.findMembershipsByUser(userName);
         for (Membership membership : membershipList)
         {
            List<String> a = memberships.get(membership.getGroupId());
            if (a == null)
            {
               memberships.put(membership.getGroupId(), a = new ArrayList<String>());
            }
            a.add(membership.getMembershipType());
         }
      }
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("groups", groups);
      params.put("memberships", memberships);
      groupsTemplate.render(params);
   }
}
