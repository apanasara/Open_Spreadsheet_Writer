
//Import required java libraries
import javax.servlet.annotation.WebServlet;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.Arrays;
import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

@WebServlet(urlPatterns="/XML", name="XL")

public class XL extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private PgSQL db = new PgSQL();
	private ResultSet rs;
	
	Element table,row,cell,data,rowH,cellR;
	Document doc;
	
	String Dvalue,id,xloc;
    NodeList nList;
    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();
    XPathExpression expr;
    
	public XL()
	{}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String SQL;
		System.out.println("Request received...");
	    response.setContentType("text/xml");
	    response.setHeader("Content-disposition", "attachment; filename=Report.xml");
	    
	    int hr = 1;//static header row
	    int mCount; //Merge Cell Count
	    
	    
	    try
	     {	    	  
	    	  //reference used : https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	    		
	    	  File fXmlFile = new File("C:\\Java\\Web Project\\Merged_Excel\\WebContent\\source\\XML\\XML_Template.xml");
	    	 
	    	  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	  dbFactory.setNamespaceAware(false);
	    	  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	  doc = (Document) dBuilder.parse(fXmlFile);
	    	  
	    	  doc.getDocumentElement().normalize();
	    	  System.out.println(doc.toString());
	    	  
	    	  
	    	  //deleting rows
	    	  nList = doc.getElementsByTagName("Row");
	    	  
	    	  while (nList.getLength() > hr) 
	    	  {
	    		    Node node = nList.item(hr);
	    		    node.getParentNode().removeChild(node);
	    	  }
	    	  
	    	  //Header Row Cleaning
	    	  rowH=(Element) nList.item(hr-1);
	    	  while (rowH.hasChildNodes())
	    	        rowH.removeChild(rowH.getFirstChild());	    	  
	    	  
	    	  table = (Element) doc.getElementsByTagName("Table").item(0);
	    	 
	    	  //declaring fields
  				Object[][] Fields1 = {
  						{"PO Line Item No","Number",8},
  						{"PO Qty","Number",9},
  						{"Dispatched Qty","Number",10},
  						{"Total MRN Qty","Number",11}
  					};
  				
  				for(Object[] f: Fields1) {cell = add_cell(rowH, "String",f[0].toString(),"header",1,(int)f[2]);}
  				
	    	  //populating rows from "PO Line Item No"
	    	  SQL = "SELECT * FROM public.\"POLine\" ORDER BY \"SAPCode\", \"PO No\", \"PO Line Item No\";";
	    	  rs = db.retriveData(SQL);
	    	  
	    	  while (rs.next())
	    	  {
	    		  //creating ROW
	    		  row =doc.createElement("Row");
	    		  table.appendChild(row);
	    		  
	    		  //assigning temporary id to Row
	    		  id = rs.getString("SAPCode")+"_"+rs.getLong("PO No")+"_"+rs.getInt("PO Line Item No");
	    		  row.setAttribute("id", id);
	    		  /* Alternatinve code
	    		   * attr = doc.createAttribute("id");
	    		   * attr.setValue(id);
	    		   * row.setAttributeNode(attr);*/
	    		  
		    	//set attribute to ROW
	    		  row.setAttribute("ss:AutoFitHeight", "0");
	    		  row.setAttribute("ss:Height", "30");
	    			
	    			for(Object[] f: Fields1) 
	    			{	
	    				String fs = f[0].toString();
	    				Dvalue = String.valueOf((fs=="PO Line Item No")?rs.getInt(fs):rs.getFloat(fs));
	    				cell = add_cell(row, f[1].toString(),Dvalue,"record",1,(int)f[2]);
	    		    }
	    	  }
	    	  
	    	  
	    	 //declaring fields
	    	Object[][] Fields2 = {
	    			{"PO No","Number",7},
	    			{"Rejected Qty","Number",12},
	    			{"Damage Qty","Number",13},
	    			{"Returned Qty","Number",14},
	    			{"MRIR Qty","Number",15},
	    			{"Overage Qty","Number",16}
	    	};
	    	
	    	cellR = (Element) rowH.getFirstChild();
	    	for(Object[] f: Fields2) {cell = add_cell(rowH,"String",String.valueOf(f[0]),"header",1,(int)f[2]);}
	    	
	    	//populating cells from "PO" into existing rows
	    	  SQL = "SELECT * FROM public.\"PO\" ORDER BY \"SAPCode\", \"PO No\";";
	    	  rs = db.retriveData(SQL);
	    	  
	    	  while (rs.next())
	    	  {
	    		  //identifying row no ROW
	    		  id = rs.getString("SAPCode")+"_"+rs.getString("PO No")+"_";
	    		  xloc="//Row[contains(@id,'" + id+ "')]";
	    		  expr = xpath.compile(xloc);
	    		  nList =  (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	    		  mCount=nList.getLength();
	    		  row = (Element) nList.item(0);
	    		  
	    		  cellR = (Element) row.getFirstChild();
	    		  for(Object[] f: Fields2) 
	    			{
	    				String fs=f[0].toString();
	    			  	Dvalue = String.valueOf((fs=="PO No")?rs.getString(fs):rs.getFloat(fs));
	    				cell = add_cell(row,f[1].toString(),Dvalue,"record",mCount,(int)f[2]);
	    		    }
	    	  }
	    	  
	    	  
	    	  //declaring fields
	    	  Object[][] Fields3 = {
	    			  {"SAPCode","String",2},
	    			  {"SAPCode LongDescription","String",3},
	    			  {"UOM","String",4},
	    			  {"Item Sub Type","String",5},
	    			  {"Material Specification","String",6},
	    		      {"Total Requested Qty","Number",17},
	    		  	  {"Total Issued Qty","Number",18},
	    		  	  {"Balance for Allocation","Number",19}
	    		};
	    	  cellR = (Element) rowH.getFirstChild();
	    	  cell = add_cell(rowH, "String","Sr No","header",1,1);
	    	  for(Object[] f: Fields3) 
	    		 cell = add_cell(rowH,"String",String.valueOf(f[0]),"header",1,(int)f[2]);
	    		  
	    	  
	    	  //populating cells from "SAPCode" into existing rows
	    	  SQL = "SELECT * FROM public.\"SAPCode\" ORDER BY \"SAPCode\";";
	    	  rs = db.retriveData(SQL);
	    	  int SrNo = 0;
	    	  while (rs.next())
	    	  {
	    		  //identifying row no ROW
	    		  
	    		  id = rs.getString("SAPCode")+"_";
	    		  xloc="//Row[contains(@id,'" + id+ "')]";
	    		  expr = xpath.compile(xloc);
	    		  nList =  (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	    		  mCount=nList.getLength();
	    		  row = (Element) nList.item(0);
	    		  
	    		  cellR = (Element) row.getFirstChild();
	    		  SrNo = SrNo + 1;
	    		  cell = add_cell(row,"Number",String.valueOf(SrNo),"record",mCount,1);
	    			
	    		  for(Object[] f: Fields3) 
	    		  {
	    				String fs=f[0].toString();	    				
	    				if (Arrays.asList("Total Requested Qty","Total Issued Qty","Balance for Allocation").contains(fs))
		    				Dvalue = String.valueOf(rs.getFloat(fs));
		    			else
		    				Dvalue = String.valueOf(rs.getString(fs));
		    			
	    				cell = add_cell(row,f[1].toString(),Dvalue,"record",mCount,(int)f[2]);
	    		    }
	    	  }
	    	  rs.close();
	    	  
	    	  //deleting temporary-ids & Sorting cells of each row as per column index
    		  
    		  String sorter="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
							"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">"+
							"  <xsl:output method=\"xml\" encoding=\"utf-8\" indent=\"yes\"/>"+
							"  <xsl:template match=\"@* | node()\">"+
							"    <xsl:copy>"+
							"      <xsl:apply-templates select=\"@* | node()\"/>"+
							"    </xsl:copy>"+
							"  </xsl:template>"+
							"  <xsl:template match=\"Row\">"+ //if namespace sensiteve then use ss:Row
							"    <xsl:copy>"+
							"      <xsl:apply-templates select=\"*\">"+
							"        <xsl:sort select=\"@Index\" order=\"ascending\" data-type=\"number\"/>"+ //if namespace sensiteve then use ss:Index
							"      </xsl:apply-templates>"+
							"    </xsl:copy>"+
							"  </xsl:template>"+
							"  <xsl:template match=\"Row/@id\"/>"+//cleaning temporary row id
							"</xsl:stylesheet>";
    		  
    		  StreamSource stylesource = new StreamSource(new StringReader(sorter));
	    	  StringWriter writer = new StringWriter();
	    	  TransformerFactory factory = TransformerFactory.newInstance();
    		  
    		  Transformer trans = factory.newTransformer(stylesource);
	    	  trans.transform(new DOMSource(doc), new StreamResult(writer));
	    	  System.out.println(writer.toString());
	    	  
	    	  //creating output stream
	    	  byte[] b = writer.toString().getBytes(StandardCharsets.UTF_8);//reference used : https://stackoverflow.com/questions/18571223/how-to-convert-java-string-into-byte
	    	  OutputStream out = response.getOutputStream();
	    	  out.write(b);
		      out.flush();
	    	  
	       } catch (Exception e){e.printStackTrace();}
	     
	    finally
	    {
		    
        }
	     
	}
	
	public Element add_cell(Element el, String type, String value, String style, int mCount, int index) 
	{
		//creating CELL
		cell = doc.createElement("Cell");		
		el.appendChild(cell);
	
    	// set attribute to CELL
		cell.setAttribute("ss:StyleID",style);
		cell.setAttribute("ss:Index",String.valueOf(index));
		
			//creating DATA
    		data =doc.createElement("Data");
    		cell.appendChild(data);
    		
    		//set attribute to DATA
			data.setAttribute("ss:Type", type);
			
			//placing DATA Value
			data.setTextContent(value);
			
		// Merging Cells
		if(mCount>1) cell.setAttribute("ss:MergeDown",String.valueOf(mCount-1));
			
    	return cell;
	}
}