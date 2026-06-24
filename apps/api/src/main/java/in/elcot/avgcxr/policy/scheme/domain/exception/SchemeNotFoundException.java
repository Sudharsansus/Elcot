package in.elcot.avgcxr.policy.scheme.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class SchemeNotFoundException extends NotFoundException {
    public SchemeNotFoundException(UUID id) {
        super("SCHEME_NOT_FOUND", "Scheme not found with id: " + id);
    }
}
