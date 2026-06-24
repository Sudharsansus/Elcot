import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { InputComponent } from './input.component';
describe('InputComponent', () => {
  let fixture: ComponentFixture<InputComponent>; let component: InputComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [InputComponent, FormsModule] }).compileComponents(); fixture = TestBed.createComponent(InputComponent); component = fixture.componentInstance; fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should show label', () => { component.label.set('Name'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('Name'); });
  it('should show error', () => { component.error.set('Required'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('Required'); });
  it('should implement ControlValueAccessor', () => { component.writeValue('test'); expect(component.value).toBe('test'); let val = ''; component.registerOnChange(v => val = v); component.onInput({ target: { value: 'hello' } } as any); expect(val).toBe('hello'); });
});
