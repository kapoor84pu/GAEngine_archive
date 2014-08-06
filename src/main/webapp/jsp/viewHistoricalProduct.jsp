<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c-rt" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="uk.co.metoffice.util.AppHelper"%>
<%@page import="uk.co.metoffice.beans.MetaData"%>
<%@page import="uk.co.metoffice.business.MetoBusiness"%>
<%@ page import="java.util.List" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">


  <head>
    <title>METO - Product details page</title>
    <jsp:include page="common/metadata.jsp" />
  </head>

 <body id="home">
  <c-rt:forEach var="aCookie" items="<%=request.getCookies()%>">
      <c:if test="${aCookie.name=='RetailPortal'}">
        <c:set var="clientId" value="${aCookie.value}"/>
      </c:if>
  </c-rt:forEach>
   <div id="wrap">
    <jsp:include page="common/header.jsp" />
      <div id="main-body">
        <div id="content">

          <!-- Page Content Starts Here -->
          <div class="mainheading" style="background: url(../img/browser.gif) no-repeat top right;">
            <h1>Page H1 title</h1>
          </div>

          <div class="chapter">
            <p style="text-align:center;"><strong>Description text</strong></p>
            <p>What is this page doing.</p>
            <h3>Meto product criteria selection</h3>
          </div>

          <div class="chapter">
            <table id="customers">
              <tr>
                <th>Date</th>
                <th>Description</th>
                <th> </th>
              </tr>

               <c:if test="${not empty requestScope.list}">
                <c:forEach items="${requestScope.list}" var="i">
                  <tr>
                    <td><c:out value="${i.validityDate}"></c:out>
                    <td><c:out value="${i.category}"></c:out>

                    <fmt:formatDate value="${i.validityDate}" pattern="yyyyMMdd" var="validDate"/>

                    <c:set var="url" value="/meto/WeatherProduct/save/${fn:toLowerCase(clientId)}::${fn:toLowerCase(i.category)}::${fn:toLowerCase(validDate)}::${fn:toLowerCase
                    (i.fileType)}"/>
                    <td>
                      <form action="${url}">
                        <input type="submit" value="Download" />
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </c:if>
            </table>
          </div>

        <!-- Page Content Ends Here -->
        </div> <!-- end #content -->
      </div> <!-- end #main-body -->

      <jsp:include page="common/rightsidebar.jsp" />
      <jsp:include page="common/footer.jsp" />
    </div> <!-- end #wrap -->
  </body>
</html>
