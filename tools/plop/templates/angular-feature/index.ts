\
import { NodePlopAPI } from 'plop';

export default function (plop: NodePlopAPI) {
  plop.setGenerator('angular-feature', {
    description: 'Create a new Angular feature module with component, service, and routing',
    prompts: [
      {
        type: 'input',
        name: 'name',
        message: 'Feature name (kebab-case):',
        validate: (value: string) => /^[a-z][a-z0-9-]*$/.test(value) ? true : 'Use kebab-case',
      },
      {
        type: 'list',
        name: 'portal',
        message: 'Which portal?',
        choices: ['public-portal', 'applicant-portal', 'admin-portal'],
      },
      {
        type: 'confirm',
        name: 'hasRouting',
        message: 'Add routing?',
        default: true,
      },
    ],
    actions: [
      {
        type: 'addMany',
        templateFiles: 'tools/plop/templates/angular-feature/**/*',
        destination: 'apps/{{portal}}/src/app/features/{{name}}',
      },
    ],
  });
}
