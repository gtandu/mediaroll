import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { HttpHeaders, HttpParams, HttpErrorResponse } from "@angular/common/http";
import { AuthentificationService } from "../authentification.service";

@Injectable()
export class MediaService {

  constructor(private http: Http, private authService: AuthentificationService) { }

}
