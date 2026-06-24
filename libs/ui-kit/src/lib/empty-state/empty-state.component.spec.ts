import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmptyStateComponent } from './empty-state.component';
describe('EmptyStateComponent', () => {
  let fixture: ComponentFixture<EmptyStateComponent>; let component: EmptyStateComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [EmptyStateComponent] }).compileComponents(); fixture = TestBed.createComponent(EmptyStateComponent); component = fixture.componentInstance; fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should display title', () => { component.title.set('Nothing here'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('Nothing here'); });
  it('should emit action click', () => { component.actionLabel.set('Add Item'); let clicked = false; component.actionClicked.subscribe(() => clicked = true); fixture.detectChanges(); fixture.nativeElement.querySelector('.avgc-empty-state__action').click(); expect(clicked).toBeTrue(); });
});
