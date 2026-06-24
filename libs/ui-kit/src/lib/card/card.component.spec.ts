import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CardComponent } from './card.component';

describe('CardComponent', () => {
  let fixture: ComponentFixture<CardComponent>;
  let component: CardComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [CardComponent] }).compileComponents(); fixture = TestBed.createComponent(CardComponent); component = fixture.componentInstance; fixture.detectChanges(); });

  it('should create', () => expect(component).toBeTruthy());
  it('should show title', () => { component.title.set('Test Card'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('Test Card'); });
  it('should show subtitle', () => { component.title.set('Title'); component.subtitle.set('Sub'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('Sub'); });
  it('should apply elevated class', () => { component.elevated.set(true); fixture.detectChanges(); expect(fixture.nativeElement.querySelector('.avgc-card').classList).toContain('avgc-card--elevated'); });
  it('should apply clickable class', () => { component.clickable.set(true); fixture.detectChanges(); expect(fixture.nativeElement.querySelector('.avgc-card').classList).toContain('avgc-card--clickable'); });
});
