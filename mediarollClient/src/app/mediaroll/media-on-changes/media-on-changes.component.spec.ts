import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MediaOnChangesComponent } from './media-on-changes.component';

describe('MediaOnChangesComponent', () => {
  let component: MediaOnChangesComponent;
  let fixture: ComponentFixture<MediaOnChangesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MediaOnChangesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MediaOnChangesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
