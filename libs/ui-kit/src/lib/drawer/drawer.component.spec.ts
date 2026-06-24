import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DrawerComponent } from './drawer.component';
describe('DrawerComponent', () => {
  let fixture: ComponentFixture<DrawerComponent>; let component: DrawerComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [DrawerComponent] }).compileComponents(); fixture = TestBed.createComponent(DrawerComponent); component = fixture.componentInstance; fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should emit closed', () => { let closed = false; component.closed.subscribe(() => closed = true); component.close(); expect(closed).toBeTrue(); });
  it('should not render when closed', () => { component.isOpen.set(false); fixture.detectChanges(); expect(fixture.nativeElement.querySelector('.avgc-drawer')).toBeNull(); });
  it('should render when open', () => { component.isOpen.set(true); fixture.detectChanges(); expect(fixture.nativeElement.querySelector('.avgc-drawer')).toBeTruthy(); });
});
