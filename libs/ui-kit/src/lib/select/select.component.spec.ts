import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectComponent } from './select.component';
describe('SelectComponent', () => {
  let fixture: ComponentFixture<SelectComponent>; let component: SelectComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [SelectComponent] }).compileComponents(); fixture = TestBed.createComponent(SelectComponent); component = fixture.componentInstance; component.options.set([{ value: 'a', label: 'Option A' }]); fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should show label', () => { component.label.set('District'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('District'); });
  it('should implement CVA', () => { component.writeValue('a'); expect(component.selectedValue).toBe('a'); let val = ''; component.registerOnChange(v => val = v); component.onSelect({ target: { value: 'a' } } as any); expect(val).toBe('a'); });
});
