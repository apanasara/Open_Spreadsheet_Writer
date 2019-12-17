<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
  
  <xsl:output method="xml" encoding="utf-8" indent="yes"/>
  
  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="ss:Row"> <!--  if namespace sensitivity is not there, then use "Row" only -->
    <xsl:copy>
      <xsl:apply-templates select="*">
        <xsl:sort select="@ss:Index" order="ascending" data-type="number"/> <!--  if namespace sensitivity is not there, then use "Index" only -->
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>
  
</xsl:stylesheet>