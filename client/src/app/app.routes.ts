import { Routes } from '@angular/router';
import { PautaListComponent } from './components/pauta-list/pauta-list.component';
import { PautaFormComponent } from './components/pauta-form/pauta-form.component';
import { PautaDetailComponent } from './components/pauta-detail/pauta-detail.component';

export const routes: Routes = [
    { path: '', redirectTo: '/pautas', pathMatch: 'full' },
    { path: 'pautas', component: PautaListComponent },
    { path: 'pautas/nova', component: PautaFormComponent },
    { path: 'pautas/:id', component: PautaDetailComponent },
];
