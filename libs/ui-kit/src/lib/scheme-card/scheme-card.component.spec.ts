import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SchemeCardComponent } from './scheme-card.component';
describe('SchemeCardComponent', () => {
  let fixture: ComponentFixture<SchemeCardComponent>; let component: SchemeCardComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [SchemeCardComponent] }).compileComponents(); fixture = TestBed.createComponent(SchemeCardComponent); component = fixture.componentInstance; component.name.set('Test Scheme'); component.maxSubsidy.set(5000000); fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy();
  it('should show scheme name', () => expect(fixture.nativeElement.textContent).toContain('Test Scheme'));
  it('should emit applyNow', () => { let clicked = false; component.applyNow.subscribe(() => clicked = true); fixture.nativeElement.querySelector('.avgc-scheme-card__btn--primary').click(); expect(clicked).toBeTrue(); });
});
