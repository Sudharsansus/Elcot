package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Talent connect configuration.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li>Category/specialization list (Animation, VFX, Gaming, Comics, XR)
 *   <li>Experience validation bounds
 *   <li>Portfolio URL validation
 * </ul>
 */
@Configuration
public class TalentconnectConfig {

  @Value(
      "${avgcxr.talent.subspecialties:Animation,VFX,Gaming,Comics,XR,AR,VR,3D Modeling,Compositing,Game Design}")
  private String subspecialties;

  @Value("${avgcxr.talent.max-experience-years:60}")
  private int maxExperienceYears;

  @Bean
  public TalentCategories talentCategories() {
    return new TalentCategories(
        java.util.Arrays.asList(subspecialties.split(",")).stream()
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList());
  }

  @Bean
  public TalentLimits talentLimits() {
    return new TalentLimits(maxExperienceYears);
  }

  public record TalentCategories(java.util.List<String> subspecialties) {}

  public record TalentLimits(int maxExperienceYears) {}
}
