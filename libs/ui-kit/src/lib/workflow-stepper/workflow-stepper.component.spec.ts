import { ComponentFixture, TestBed } from '@angular/core/testing';
import { WorkflowStepperComponent } from './workflow-stepper.component';
describe('WorkflowStepperComponent', () => {
  let fixture: ComponentFixture<WorkflowStepperComponent>; let component: WorkflowStepperComponent;
  const steps = [{ label: 'Personal', status: 'completed' as const }, { label: 'Project', status: 'active' as const }, { label: 'Documents', status: 'pending' as const }];
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [WorkflowStepperComponent] }).compileComponents(); fixture = TestBed.createComponent(WorkflowStepperComponent); component = fixture.componentInstance; component.steps.set(steps); component.currentStep.set(1); fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should render all steps', () => { expect(fixture.nativeElement.querySelectorAll('.avgc-stepper__item').length).toBe(3); });
  it('should emit stepClicked', () => { let idx = -1; component.stepClicked.subscribe(i => idx = i); component.stepClicked.emit(2); expect(idx).toBe(2); });
});
