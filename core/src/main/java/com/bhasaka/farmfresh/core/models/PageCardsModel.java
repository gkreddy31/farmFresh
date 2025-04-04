package com.bhasaka.farmfresh.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageCardsModel {

    private static final String FEATURED_IMAGE_NODE = "cq:featuredimage";
    private static final String FILE_REFERENCE = "fileReference";

    @ValueMapValue
    private String rootPagePath;

    @SlingObject
    private ResourceResolver resolver;

    private final List<PageCard> pageCards = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (StringUtils.isBlank(rootPagePath) || resolver == null) {
            return;
        }
        Resource pageResource = resolver.getResource(rootPagePath);
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        if (pageManager != null && pageResource != null) {
            Page parentPage = pageManager.getContainingPage(pageResource);
            if (parentPage != null) {
                fetchAllChildPages(parentPage);
            }
        }
    }

    private void fetchAllChildPages(Page parentPage) {
        for (Iterator<Page> it = parentPage.listChildren(); it.hasNext(); ) {
            Page childPage = it.next();
            String title = childPage.getTitle();
            String description = childPage.getDescription();
            String imagePath = "";
            Resource pageImageResource = childPage.getContentResource().getChild(FEATURED_IMAGE_NODE);
            if (pageImageResource != null) {
                imagePath = pageImageResource.getValueMap().get(FILE_REFERENCE, String.class);
            }
            pageCards.add(new PageCard(title, description, imagePath));
            fetchAllChildPages(childPage);
        }
    }

    public List<PageCard> getPageCards() {
        return pageCards;
    }
}
