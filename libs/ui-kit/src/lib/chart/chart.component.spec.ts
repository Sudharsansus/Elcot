import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChartComponent } from './chart.component';

describe('ChartComponent', () => {
  let fixture: ComponentFixture<ChartComponent>;
  let component: ChartComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [ChartComponent] }).compileComponents(); fixture = TestBed.createComponent(ChartComponent); component = fixture.componentInstance; fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should have default height', () => { expect(component.height()).toBe('400px'); });
  it('should have default type bar', () => { expect(component.type()).toBe('bar'); });
});
