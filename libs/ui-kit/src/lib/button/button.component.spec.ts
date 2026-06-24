import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ButtonComponent } from './button.component';

describe('ButtonComponent', () => {
  let fixture: ComponentFixture<ButtonComponent>;
  let component: ButtonComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({ imports: [ButtonComponent] }).compileComponents();
    fixture = TestBed.createComponent(ButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => expect(component).toBeTruthy());

  it('should render content', () => { fixture.nativeElement.textContent = 'Click Me'; fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('Click Me'); });

  it('should emit clicked event', () => {
    let clicked = false;
    component.clicked.subscribe(() => clicked = true);
    fixture.nativeElement.querySelector('button').click();
    expect(clicked).toBeTrue();
  });

  it('should not emit when disabled', () => {
    component.variant.set('primary'); component.disabled.set(true);
    let clicked = false;
    component.clicked.subscribe(() => clicked = true);
    fixture.detectChanges();
    fixture.nativeElement.querySelector('button').click();
    expect(clicked).toBeFalse();
  });

  it('should show spinner when loading', () => {
    component.loading.set(true); fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.avgc-btn__spinner')).toBeTruthy();
  });

  it('should apply variant class', () => {
    component.variant.set('danger'); fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('button').classList).toContain('avgc-btn--danger');
  });

  it('should be accessible with aria-label', () => {
    component.ariaLabel.set('Submit form'); fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('button').getAttribute('aria-label')).toBe('Submit form');
  });
});
