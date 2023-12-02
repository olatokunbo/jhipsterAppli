import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ValeurDetailComponent } from './valeur-detail.component';

describe('Valeur Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValeurDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ValeurDetailComponent,
              resolve: { valeur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ValeurDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load valeur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ValeurDetailComponent);

      // THEN
      expect(instance.valeur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
