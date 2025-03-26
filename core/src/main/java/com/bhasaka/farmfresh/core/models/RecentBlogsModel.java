package com.bhasaka.farmfresh.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecentBlogsModel {

    @ValueMapValue
    private String blogTitle;

    @ValueMapValue
    private String[] articlePagePaths;

    @SlingObject
    ResourceResolver resolver;

    List<BlogsModel> blogs;

    @PostConstruct
    public void init() {
        if (articlePagePaths != null && articlePagePaths.length > 0) {
            List<BlogsModel> blogs = new ArrayList<>();
            for (String articlePagePath : articlePagePaths) {
                Resource articlePage = resolver.getResource(articlePagePath + "/jcr:content");
                if (articlePage != null) {
                    BlogsModel blogModel = articlePage.adaptTo(BlogsModel.class);
                    if (blogModel != null) {
                        String imagePath = getImagePath(articlePage);
                        blogModel.setImagePath(imagePath);
                        blogs.add(blogModel);
                    }
                }

            }
            this.blogs = blogs;
        }
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public String[] getArticlePagePaths() {
        return articlePagePaths;
    }

    public List<BlogsModel> getBlogs() {
        return blogs;
    }

    private String getImagePath(Resource articlePage) {
        Resource imageResource = resolver.getResource(articlePage.getPath() + "/root/container/blog_banner");
        if (imageResource != null) {
            String image = imageResource.getValueMap().get("fileReference", String.class);
            return image;
        }
        return null;
    }

}
