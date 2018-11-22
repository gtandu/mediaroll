import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MediarollModule } from './mediaroll/mediaroll.module';
import { AuthentificationService } from './services/authentification.service';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';
import * as $ from 'jquery';

import * as bootstrap from 'bootstrap';

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
  ],
  imports: [
    BrowserModule,
    HttpModule,
    FormsModule,
    MediarollModule,
    AppRoutingModule
  ],
  providers: [AuthentificationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
