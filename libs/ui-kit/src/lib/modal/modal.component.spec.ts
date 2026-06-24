import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModalComponent } from './modal.component';
describe('ModalComponent', () => {
  let fixture: ComponentFixture<ModalComponent>; let component: ModalComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [ModalComponent] }).compileComponents(); fixture = TestBed.createComponent(ModalComponent); component = fixture.componentInstance; fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy();
  it('should render when open', () => { component.isOpen.set(true); component.title.set('Confirm'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('Confirm'); });
  it('should emit closed', () => { let closed = false; component.closed.subscribe(() => closed = true); component.close(); expect(closed).toBeTrue(); });
  it('should emit confirmed', () => { let confirmed = false; component.confirmed.subscribe(() => confirmed = true); component.confirm(); expect(confirmed).toBeTrue(); });
});
