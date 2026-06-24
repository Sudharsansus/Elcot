package in.elcot.avgcxr.ecosystem.freelancerregistry.application.command;

import java.util.Map;

public record CreateFreelancerProfileCommand(
    Map<String, Object> fields
) {}
