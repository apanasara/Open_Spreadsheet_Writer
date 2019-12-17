# Open_Spreadsheet_Writer
Merging fields of multiple Tables in DB to write Spreadsheet report. Spreadsheets are in Open spreadsheet format, hence can be opned into Ms Excel, Liber Office etc. 



## Used tech
1. Creation of XML based on XML-Spreadsheet requirements (Reference : https://docs.microsoft.com/en-us/previous-versions/office/developer/office-xp/aa140066(v=office.10))
2. XPATH for query in XML
3. XSLT for XML Transformation

## Benefit
*	Almost all language supports XML (java, Python, VB, C# any) hence report can be created on any server
*	XML-Spreadsheet has capability to create Excel in any format (feature list on https://support.office.com/en-us/article/excel-formatting-and-features-that-are-not-transferred-to-other-file-formats-8fdd91a3-792e-4aef-a5bb-46f603d0e585#bm8)

## Example

Here, example is elobrated on Material management where single report is generated thru stitching 3 different tables based on Primery-key.
* *Table-1* has information on Material Issuance from store
* *Table-2* has information on Purchase Order
* *Table-3* has information on Detailed break up of Purchase Orders with respect to delivery from Supplier


|	Table	|	Description	|	Primary Key	|	Relationship	|
|	-------	|	---	|	---	|	---	|
|	Table-1	|	Material Issuance from store	|	SAP Code(Item ID)	|		|
|	Table-2	|	Purchase Order	|	PO No	|	Table-1:SAP Code	|
|	Table-3	|	Detailed break up of Purchase Orders with respect to delivery from Supplier	|	PO No+Line Item No	|	Table-2:PO No	|


**Data Structure**

![Data Structure](Images/Data%20Structure.jpg)


## Key Points in Code
1.	ORDER BY Sequence should be logically asper data structure diagram given above
2.	Rows should be created by using table which have primary key of all table (in our case POLineItem table)
3.	Each Row is assigned Temporary for identification based on primary key of all relevant tables. This temporary ID will be useful for populating data from other tables to relevant row as well as identifying merge range. (at the end temporary id is removed by XSLT).


**Client-side screen**
Tested on Eclipse Kepler IDE (refer xml-spreadsheet(report) which is generated thru Java Tomcat server)

![Client Screen](Images/Screen%20Shot.jpg)

***Extracted Report*** : [Open Spreadsheet](Report(XML-Spreadsheet).xml)

**Project Setup on Eclilpse**

![Eclipse](Images/Eclipse%20setup.jpg)

**Jar files required**

* commons-logging-1.2-javadoc.jar
* commons-logging-1.2.jar
* org-apache-commons-codec.jar
* org.apache.commons.httpclient.jar
* postgresql-42.2.5.jre7(4.1).jar

**DB Tables on Postgress**

![DB Tables](Images/Postgress%20Table%20Structure.png)
