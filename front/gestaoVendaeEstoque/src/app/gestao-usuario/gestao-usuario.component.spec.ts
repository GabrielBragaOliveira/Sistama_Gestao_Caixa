import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestaoUsuarioComponent } from './gestao-usuario.component';

describe('GestaoUsuarioComponent', () => {
  let component: GestaoUsuarioComponent;
  let fixture: ComponentFixture<GestaoUsuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestaoUsuarioComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GestaoUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
