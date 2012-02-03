package org.gatein.portal.people.portlet;

import org.w3c.dom.Element;

import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import java.io.IOException;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class ResponseFilter implements RenderFilter
{

   public void init(FilterConfig filterConfig) throws PortletException
   {
   }

   public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException
   {
      Element jQuery1 = response.createElement("script");
      jQuery1.setAttribute("type", "text/javascript");
      jQuery1.setAttribute("src", request.getContextPath() + "/js/jquery-1.7.1.min.js");
      response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jQuery1);

      //
      Element jQuery2 = response.createElement("script");
      jQuery2.setAttribute("type", "text/javascript");
      jQuery2.setAttribute("src", request.getContextPath() + "/js/less-1.2.1.min.js");
      response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jQuery2);

      //
      Element css1 = response.createElement("link");
      css1.setAttribute("rel", "stylesheet/less");
      css1.setAttribute("href", request.getContextPath() + "/css/main.less");
      response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, css1);

      //
      chain.doFilter(request, response);
   }

   public void destroy()
   {
   }
}
