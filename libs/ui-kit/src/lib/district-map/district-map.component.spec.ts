import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DistrictMapComponent } from './district-map.component';

describe('DistrictMapComponent', () => {
  let fixture: ComponentFixture<DistrictMapComponent>;
  let component: DistrictMapComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [DistrictMapComponent] }).compileComponents(); fixture = TestBed.createComponent(DistrictMapComponent); component = fixture.componentInstance; component.districts.set([{ code:'CHE', name:'Chennai', nameTamil:'சென்னை', value:500 }]); fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should emit district on select', () => { let selected: any; component.districtSelected.subscribe(d => selected = d); component.selectDistrict({ code:'CHE', name:'Chennai', nameTamil:'சென்னை', value:500 }); expect(selected.code).toBe('CHE'); });
});
