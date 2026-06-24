import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ThemeToggleComponent } from './theme-toggle.component';
describe('ThemeToggleComponent', () => {
  let fixture: ComponentFixture<ThemeToggleComponent>; let component: ThemeToggleComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [ThemeToggleComponent] }).compileComponents(); fixture = TestBed.createComponent(ThemeToggleComponent); component = fixture.componentInstance; fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should toggle theme', () => { const initial = component.isDark(); component.toggle(); expect(component.isDark()).toBe(!initial); });
});
