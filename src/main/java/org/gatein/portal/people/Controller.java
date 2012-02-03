package org.gatein.portal.people;

import org.exoplatform.services.organization.OrganizationService;
import org.juzu.Resource;
import org.juzu.View;
import org.juzu.Path;
import org.juzu.template.Template;
import javax.inject.Inject;
import java.io.IOException;

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

   @Inject
   public Controller(OrganizationService orgService)
   {
      this.orgService = orgService;
   }

   @View
   public void index() throws IOException
   {
      index.render();
   }
   
   @Resource
   public void users() throws IOException
   {
      users.render();
   }
}
