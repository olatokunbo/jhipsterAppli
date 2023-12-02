import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ValeurService } from '../service/valeur.service';

import { ValeurComponent } from './valeur.component';

describe('Valeur Management Component', () => {
  let comp: ValeurComponent;
  let fixture: ComponentFixture<ValeurComponent>;
  let service: ValeurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'valeur', component: ValeurComponent }]), HttpClientTestingModule, ValeurComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ValeurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ValeurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ValeurService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.valeurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to valeurService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getValeurIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getValeurIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
