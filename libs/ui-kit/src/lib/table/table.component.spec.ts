import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TableComponent } from './table.component';
describe('TableComponent', () => {
  let fixture: ComponentFixture<TableComponent>; let component: TableComponent;
  beforeEach(async () => { await TestBed.configureTestingModule({ imports: [TableComponent] }).compileComponents(); fixture = TestBed.createComponent(TableComponent); component = fixture.componentInstance; component.columns.set([{ key: 'name', label: 'Name', sortable: true }]); component.data.set([{ name: 'Test' }]); component.totalElements.set(1); component.totalPages.set(1); fixture.detectChanges(); });
  it('should create', () => expect(component).toBeTruthy());
  it('should render columns and data', () => { expect(fixture.nativeElement.textContent).toContain('Name'); expect(fixture.nativeElement.textContent).toContain('Test'); });
  it('should emit sortChanged', () => { let sort: any; component.sortChanged.subscribe(s => sort = s); component.onSort('name'); expect(sort).toEqual({ key: 'name', direction: 'ASC' }); });
  it('should show empty message', () => { component.data.set([]); component.emptyMessage.set('No rows'); fixture.detectChanges(); expect(fixture.nativeElement.textContent).toContain('No rows'); });
});
