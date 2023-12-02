import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UniteAdministrativeDetailComponent } from './unite-administrative-detail.component';

describe('UniteAdministrative Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UniteAdministrativeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UniteAdministrativeDetailComponent,
              resolve: { uniteAdministrative: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UniteAdministrativeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load uniteAdministrative on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UniteAdministrativeDetailComponent);

      // THEN
      expect(instance.uniteAdministrative).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
