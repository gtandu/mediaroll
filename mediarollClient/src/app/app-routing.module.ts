import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';
import { LoginFormComponent } from './login-form/login-form.component';

// routes
const appRoutes: Routes = [
	{ path: 'login', component: LoginFormComponent },
	{ path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
	imports: [
		RouterModule.forRoot(appRoutes)
	],
	exports: [
		RouterModule
	]
})
export class AppRoutingModule { }