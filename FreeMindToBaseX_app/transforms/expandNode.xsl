<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!--get all nodes where there is an attribute named "TEXT"-->
    <xsl:template match="node[@TEXT]">     
        <xsl:copy>
            <!--get all content except the attribute "TEXT"-->
            <xsl:apply-templates select="@*[name()!='TEXT']"/>
                <xsl:element name="richcontent"> <xsl:attribute name="TYPE">NODE</xsl:attribute>
                    <xsl:element name="html">
                        <xsl:element name="head"/>
                        <xsl:element name="body">
                            <xsl:element name="p">
                                <!--get the value of the attribute named "TEXT"-->
                                <xsl:value-of select="@TEXT"/>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
<!--    get the remaining nodes OR attributes-->
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>