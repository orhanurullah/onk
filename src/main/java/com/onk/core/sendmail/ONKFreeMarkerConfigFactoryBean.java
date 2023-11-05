package com.onk.core.sendmail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import java.io.IOException;
import java.util.Properties;


@Component
public class ONKFreeMarkerConfigFactoryBean extends FreeMarkerConfigurationFactory
        implements FactoryBean<Configuration>, InitializingBean, ResourceLoaderAware
{
    private Configuration configuration;

    public ONKFreeMarkerConfigFactoryBean() {
        this.setTemplateLoaderPath("classpath:/mailtemplates");
        this.setDefaultEncoding("utf-8");
        Properties properties = new Properties();
        properties.put("locale", "tr_TR");
        this.setFreemarkerSettings(properties);
        this.setPreferFileSystemAccess(false);

    }

    public Template getTemplate(String name) throws IOException {
        assert this.getObject() != null;
        return this.getObject().getTemplate(name);
    }

    public void afterPropertiesSet() throws IOException, TemplateException
    {
        this.configuration = this.createConfiguration();
    }

    public Configuration getObject() {
        return this.configuration;
    }

    public Class<? extends Configuration> getObjectType() {
        return Configuration.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
