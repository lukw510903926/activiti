<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="sso" uri="/WEB-INF/tlds/sso.tld" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="resList" value=","></c:set>
<c:forEach  items="${sessionScope.resourceList}" var="res">
	<c:set var="resList" value="${resList}${res},"></c:set>
</c:forEach>
<c:set var="baseCtx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
<c:set var="ctx" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<c:set var="ctxPlugins" value="${pageContext.request.contextPath}/plugins"/>
<c:set var="themePath" value="${pageContext.request.contextPath}/themes/default"/>
<c:set var="unmpUrl" value="http://super100.gmcc.net/_layouts/UnmpTransfer.aspx?UnmpReturnUrl=http://12583.gmcc.net/RMS"/>



