package com.bhasaka.farmfresh.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Component(
        service = WorkflowProcess.class,
        property = { "process.label=Vanity URL Service" }
)
public class VanityURLServiceImpl implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(VanityURLServiceImpl.class);

    private static final String VANITY_PATH_PROPERTY = "sling:vanityPath";

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        String pagePath = workItem.getWorkflowData().getPayload().toString();
        try (ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class)) {
            if(Objects.nonNull(resolver)) {
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                Page page = pageManager.getPage(pagePath);
                if(Objects.nonNull(page)) {
                    Resource pageResource = page.getContentResource();
                    ModifiableValueMap properties = pageResource.adaptTo(ModifiableValueMap.class);
                    String parentPath = page.getParent() != null ? page.getParent().getPath() : "";
                    String modifiedVanityPath = page.getPath().replaceFirst(parentPath + "/", "");
                    properties.put(VANITY_PATH_PROPERTY, modifiedVanityPath);
                    resolver.commit();
                    LOG.info("Successfully updated vanity path for page: {}", pagePath);
                }
            }
        } catch (PersistenceException e) {
            LOG.error("Error while committing changes to vanity path", e);
            throw new WorkflowException("Failed to commit vanity path update", e);
        }
    }
}
