package l16.frontend.app;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * 
 */
class TemplateProcessor
{
    private static final String HTML_DIR = "/tml/";

    private Configuration configuration;

    void init() throws IOException
    {
    	 if (configuration != null)
    		 return;
    	 
         configuration = new Configuration(Configuration.VERSION_2_3_28);
         configuration.setClassForTemplateLoading(this.getClass(), HTML_DIR);
         configuration.setDefaultEncoding("UTF-8");
    }
    
    String getPage(String filename, Map<String, Object> data) throws IOException
    {
    	init();
    	
        try (Writer stream = new StringWriter())
        {
            Template template = configuration.getTemplate(filename);
            template.process(data, stream);
            return stream.toString();
        }
        catch (TemplateException e) 
        {
            throw new IOException(e);
        }
    }
}
