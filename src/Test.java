import com.boomi.execution.*;
import java.io.InputStream;
import java.util.Properties;

public class Test {
    public static void main(String[] args) {
        String inputFilePath = System.getProperty("user.dir") + "/testfiles/in/";
        String outputFilePath = System.getProperty("user.dir") + "/testfiles/out/";
        DataContext dataContext = new DataContext(outputFilePath);
        dataContext.attachFiles(inputFilePath);
        ExecutionUtil ExecutionUtil = new ExecutionUtil();

        ExecutionUtil.setExecutionProperty("com.boomi.execution.lastrun", new java.util.Date().toString());

        // Set Process property
        ExecutionUtil.setProcessProperty("e664d4fc-2e22-4898-ba05-1a898409a813", "sample_property", "sample_property-value");

        // Set Dynamic Process Property
        ExecutionUtil.setDynamicProcessProperty("DPP_sample", "DPP_sample-value", false);

        // Set Document Properties for all incoming messages
        for (int i = 0; i < dataContext.getDataCount(); i++)
            dataContext.addDocumentPropertyValues(i, "connector.track.disk.filename", inputFilePath + "_" + i);

        // Set Dynamic Document Properties for all incoming messages
        for (int i = 0; i < dataContext.getDataCount(); i++)
            dataContext.addDynamicDocumentPropertyValues(i, "DDP-sample", inputFilePath + "DDP_sample-value-" + i);

        Logger logger = ExecutionUtil.getBaseLogger();


        for (int i = 0; i < dataContext.getDataCount(); i++) {
            InputStream is = dataContext.getStream(i);
            Properties props = dataContext.getProperties(i);

            props.setProperty("document.dynamic.userdefined.filecontent", is.toString());

            logger.info("DDP: " + props.getProperty("document.dynamic.userdefined.DDP-sample"));
            logger.info("PP: " + ExecutionUtil.getProcessProperty("e664d4fc-2e22-4898-ba05-1a898409a813", "sample_property"));
            logger.info("EP: " + ExecutionUtil.getExecutionProperty("com.boomi.execution.lastrun"));
            logger.info("DPP: " + ExecutionUtil.getDynamicProcessProperty("DPP_sample"));
            dataContext.storeStream(is, props);
        }

    }
}
