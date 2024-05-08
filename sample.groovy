//=============================================================================

//Mock section for testing outside of Boomi. Remove when adding to Data Process shape.
import com.boomi.execution.DataContext
import com.boomi.execution.Logger
// Place directory for multiple files and file name for single file
String inPath = "${System.getProperty("user.dir")}/testfiles/in"
String outPath = "${System.getProperty("user.dir")}/testfiles/out"
dataContext = new DataContext(outPath) //Custom Class to mock Boomi's internal dataContext
dataContext.attachFiles(inPath)
ExecutionUtil ExecutionUtil = new ExecutionUtil() //Custom Class to mock Boomi's internal ExecutionUtil

//Set any Execution Properties that the Boomi process would add
ExecutionUtil.setExecutionProperty("com.boomi.execution.lastrun", new java.util.Date().toString());

// Set any Dynamic Process Properties that the Boomi process would add. 
ExecutionUtil.setDynamicProcessProperty("dpp_sample", "dpp_sample-value", false)

// Set any Process properties that the Boomi process would add. 
ExecutionUtil.setProcessProperty("e664d4fc-2e22-4898-ba05-1a898409a813", "sample_property", "sample-property-value");

// Add any Dynamic Document Properties that the Boomi process would add.
// If setting DDPs for multiple files, then index needs to be set. Index range starts at 0.
dataContext.addDynamicDocumentPropertyValues(0, "ddp_sample", "ddp_sample-value-0") //First document is 0
dataContext.addDynamicDocumentPropertyValues(1, "ddp_sample", "ddp_sample-value-1") //First document is 1

// Add any Document Properties that the Boomi process would add.
// If setting DDPs for multiple files, then index needs to be set. Index range starts at 0.
dataContext.addDocumentPropertyValues(0, "connector.track.disk.filename", "input-file-name-0") //First document is 0
dataContext.addDocumentPropertyValues(0, "connector.track.disk.filename", "input-file-name-1") //First document is 1

// End of mock section. Boomi script below this 
//=============================================================================
import com.boomi.execution.ExecutionUtil; 
import java.util.Properties;
import java.io.InputStream;
import groovy.json.*

Logger logger = ExecutionUtil.getBaseLogger();

for( int i = 0; i < dataContext.getDataCount(); i++ ) {
    InputStream is = dataContext.getStream(i);
    Properties props = dataContext.getProperties(i);
    logger.info("dpp_sample: " + ExecutionUtil.getDynamicProcessProperty("dpp_sample"));
    logger.info("ddp_sample: " + props.getProperty("ddp_sample"));
    logger.info("sample_property: " + ExecutionUtil.getProcessProperty("e664d4fc-2e22-4898-ba05-1a898409a813", "sample_property"));
    logger.info("lastrun: " + ExecutionUtil.getExecutionProperty("com.boomi.execution.lastrun"));
    
    //logger.info ("File Contents: " + is.text);

    
    dataContext.storeStream(is, props);
}

