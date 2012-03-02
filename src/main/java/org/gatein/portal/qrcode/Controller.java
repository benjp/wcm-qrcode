package org.gatein.portal.qrcode;

import org.juzu.Path;
import org.juzu.View;
import org.juzu.template.Template;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class Controller
{

   /** . */
   private static final Map<String, String> PROFILE_MAPPING = new HashMap<String, String>();
   
   static
   {
      PROFILE_MAPPING.put("job", "user.jobtitle");
      PROFILE_MAPPING.put("cell-phone", "user.home-info.telecom.mobile.number");
      PROFILE_MAPPING.put("work-phone", "user.business-info.telecom.mobile.number");
      PROFILE_MAPPING.put("country", "user.home-info.postal.country");
   }

   /** . */
   @Inject
   @Path("index.gtmpl")
   Template indexTemplate;

   @Inject
   public Controller()
   {
   }

   @View
   public void index() throws IOException
   {
      indexTemplate.render();
   }

}
