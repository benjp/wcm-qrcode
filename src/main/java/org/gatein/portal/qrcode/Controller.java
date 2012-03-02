package org.gatein.portal.qrcode;

import org.juzu.Path;
import org.juzu.View;
import org.juzu.template.Template;

import javax.inject.Inject;
import java.io.IOException;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class Controller
{

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
