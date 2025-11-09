import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestaoEstoqueComponent } from './gestao-estoque.component';

describe('GestaoEstoqueComponent', () => {
  let component: GestaoEstoqueComponent;
  let fixture: ComponentFixture<GestaoEstoqueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestaoEstoqueComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GestaoEstoqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
