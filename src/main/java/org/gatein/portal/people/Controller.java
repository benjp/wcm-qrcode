package org.gatein.portal.people;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.juzu.Resource;
import org.juzu.View;
import org.juzu.Path;
import org.juzu.template.Template;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class Controller
{

   /** . */
   @Inject
   @Path("index.gtmpl")
   Template index;

   /** . */
   @Inject
   @Path("users.gtmpl")
   Template users;

   /** . */
   private final OrganizationService orgService;

   /** . */
   private final UserHandler userHandler;

   @Inject
   public Controller(OrganizationService orgService)
   {
      this.orgService = orgService;
      this.userHandler = orgService.getUserHandler();
   }

   @View
   public void index() throws IOException
   {
      index.render();
   }
   
   @Resource
   public void users() throws Exception
   {
      int from = 0;
      int size = 10;
      ListAccess<User> userList = userHandler.findAllUsers();
      int length = Math.min(from + size, userList.getSize());
      users.render(Collections.singletonMap("users", userList.load(from, length)));
   }
}
