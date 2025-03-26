package com.bhasaka.farmfresh.core.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BlogsModel {

    @ValueMapValue
    @Named("jcr:title")
    private String title;

    @ValueMapValue
    @Named("jcr:description")
    private String description;

    @ValueMapValue
    @Named("cq:lastModified")
    private Date lastModified;

    private String imagePath;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return formatDate(lastModified);
    }

    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            return sdf.format(date);
        }
        return "";
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
