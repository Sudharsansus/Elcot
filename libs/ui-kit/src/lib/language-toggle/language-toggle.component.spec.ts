import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LanguageToggleComponent } from './language-toggle.component';
import { TranslationService } from '@avgc-xr/i18n';
import { of } from 'rxjs';
describe('LanguageToggleComponent', () => {
  let fixture: ComponentFixture<LanguageToggleComponent>; let component: LanguageToggleComponent;
  beforeEach(async () => {
    TestBed.configureTestingModule({ imports: [LanguageToggleComponent], providers: [{ provide: TranslationService, useValue: { locale: () => 'en', setLocale: jest.fn(), toggleLocale: jest.fn() } }] }).compileComponents();
    fixture = TestBed.createComponent(LanguageToggleComponent); component = fixture.componentInstance; fixture.detectChanges();
  });
  it('should create', () => expect(component).toBeTruthy());
  it('should toggle locale', () => { const spy = jest.spyOn(component as any, 'toggle'); component.toggle(); expect(spy).toHaveBeenCalled(); });
});
