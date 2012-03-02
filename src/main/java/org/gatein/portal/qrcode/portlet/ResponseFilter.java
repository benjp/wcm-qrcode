package org.gatein.portal.qrcode.portlet;

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
      //
      Element jq = response.createElement("script");
      jq.setAttribute("type", "text/javascript");
      jq.setAttribute("src", request.getContextPath() + "/js/jquery-1.7.1.min.js");
      response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jq);

      Element jqq = response.createElement("script");
      jqq.setAttribute("type", "text/javascript");
      jqq.setAttribute("src", request.getContextPath() + "/js/jquery.qrcode.min.js");
      response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jqq);

      //
      chain.doFilter(request, response);
   }

   public void destroy()
   {
   }
}
