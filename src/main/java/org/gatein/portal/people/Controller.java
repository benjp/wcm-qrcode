package org.gatein.portal.people;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
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
import java.util.Collections;
import java.util.List;

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

   @Inject
   public Controller(OrganizationService orgService)
   {
      this.orgService = orgService;
      this.userHandler = orgService.getUserHandler();
      this.groupHandler = orgService.getGroupHandler();
   }

   @View
   public void index() throws IOException
   {
      indexTemplate.render();
   }
   
   @Resource
   public void users(String name) throws Exception
   {
      if (name != null)
      {
         name = name.trim();
      }
      Query query = new Query();
      if (name != null && name.length() > 0)
      {
         query.setUserName("*" + name + "*");
      }
      ListAccess<User> list = userHandler.findUsersByQuery(query);
      int from = 0;
      int size = 10;
      int length = Math.min(from + size, list.getSize());
      usersTemplate.render(Collections.singletonMap("users", list.load(from, length)));
   }

   @Resource
   public void groups(String name) throws Exception
   {
      if (name != null)
      {
         name = name.trim();
      }
      @SuppressWarnings("unchecked")
      List<Group> list = (List<Group>)groupHandler.getAllGroups();
      List<Group> groups = new ArrayList<Group>(list.size());
      for (Group group : list)
      {
         if (name == null || name.length() == 0 || group.getGroupName().contains(name))
         {
            groups.add(group);
         }
      }
      groupsTemplate.render(Collections.singletonMap("groups", groups));
   }
}
