\
import { NodePlopAPI } from 'plop';

export default function (plop: NodePlopAPI) {
  plop.setGenerator('spring-service', {
    description: 'Create a new Spring Boot bounded context',
    prompts: [
      {
        type: 'input',
        name: 'contextName',
        message: 'Bounded context name (kebab-case, e.g. scheme-management):',
        validate: (value: string) => /^[a-z][a-z0-9-]*$/.test(value) ? true : 'Use kebab-case',
      },
      {
        type: 'input',
        name: 'entityName',
        message: 'Primary entity name (PascalCase, e.g. Scheme):',
      },
      {
        type: 'confirm',
        name: 'withWorkflow',
        message: 'Integrate with Flowable workflow?',
        default: false,
      },
    ],
    actions: [
      {
        type: 'addMany',
        templateFiles: 'tools/plop/templates/spring-service/**/*',
        destination: 'apps/api/src/main/java/in/elcot/avgcxr/{{contextName}}',
      },
      {
        type: 'add',
        templateFile: 'tools/plop/templates/spring-service/migration.sql.hbs',
        path: 'apps/api/src/main/resources/db/migration/V{{timestamp}}__create_{{contextName}}_tables.sql',
      },
    ],
  });
}
