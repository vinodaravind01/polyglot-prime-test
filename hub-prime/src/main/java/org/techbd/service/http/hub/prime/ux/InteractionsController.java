package org.techbd.service.http.hub.prime.ux;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.techbd.orchestrate.sftp.SftpManager;
import org.techbd.service.http.hub.prime.route.RouteMapping;
import org.techbd.udi.UdiPrimeJpaConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@Tag(name = "Tech by Design Hub Interactions UX API",
        description = "Tech by Design Hub Interactions UX API")
public class InteractionsController {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(InteractionsController.class.getName());

    private final Presentation presentation;
    private final SftpManager sftpManager;

    public InteractionsController(final Presentation presentation,
            @SuppressWarnings("PMD.UnusedFormalParameter") final UdiPrimeJpaConfig udiPrimeJpaConfig,
            final SftpManager sftpManager) {
        this.presentation = presentation;
        this.sftpManager = sftpManager;
    }

    @GetMapping("/interactions")
    @RouteMapping(label = "Interactions", siblingOrder = 20)
    public String observeInteractions() {
        return "redirect:/interactions/httpsfhir";
    }

    @GetMapping("/interactions/httpsfhir")
    @RouteMapping(label = "FHIR via HTTPs", title = "FHIR Interactions via HTTPs", siblingOrder = 20)
    public String httpsfhir(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/httpsfhir", model, request);
    }

    @GetMapping("/interactions/httpsfailed")
    @RouteMapping(label = "FHIR via HTTPs FAILED", title = "FHIR Interactions via HTTPs (POST to SHIN-NY Failures)", siblingOrder = 30)
    public String httpsFailed(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/httpsfailed", model, request);
    }

    @GetMapping("/interactions/sftp")
    @RouteMapping(label = "CSV via SFTP (recent 10 egress)", title = "CSV Files via SFTP (egress directory)", siblingOrder = 40)
    public String sftp(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/sftp", model, request);
    }

    @Operation(summary = "Recent Orchctl Interactions")
    @GetMapping("/interactions/orchctl")
    @RouteMapping(label = "CSV via SFTP (DB)", title = "CSV Files via SFTP (in PostgreSQL DB)", siblingOrder = 50)
    public String orchctl(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/orchctl", model, request);
    }

    @Operation(summary = "Recent SFTP Interactions")
    @GetMapping("/support/interaction/sftp/recent.json")
    @ResponseBody
    public List<?> observeRecentSftpInteractions(
            @Parameter(description = "Optional variable to mention the number of entries to be fetched. If no value is specified, 10 entries will be taken by default.", required = true)
            final @RequestParam(defaultValue = "10") int limitMostRecent) {
        return sftpManager.tenantEgressSessions(limitMostRecent);
    }

    @GetMapping("/interactions/https")
    @RouteMapping(label = "HTTP Interactions", title = "Interactions via HTTPs", siblingOrder = 60)
    public String https(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/https", model, request);
    }

    @GetMapping("/interactions/observe")
    @RouteMapping(label = "Performance Overview", title = "Performance Overview", siblingOrder = 70)
    public String osberve(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/observe", model, request);
    }

    @GetMapping("/interactions/provenance")
    @RouteMapping(label = "Provenance", title = "Provenance", siblingOrder = 80)
    public String provenance(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/provenance", model, request);
    }

    @GetMapping("/interactions/user")
    @RouteMapping(label = "User Sessions", title = "User", siblingOrder = 90)
    public String user(final Model model, final HttpServletRequest request) {
        return presentation.populateModel("page/interactions/user", model, request);
    }
}
