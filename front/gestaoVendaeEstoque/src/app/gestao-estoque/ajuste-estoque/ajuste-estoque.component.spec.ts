import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjusteEstoqueComponent } from './ajuste-estoque.component';

describe('AjusteEstoqueComponent', () => {
  let component: AjusteEstoqueComponent;
  let fixture: ComponentFixture<AjusteEstoqueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjusteEstoqueComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AjusteEstoqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
