import { Component, OnInit, AfterViewChecked } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit, AfterViewChecked {

  ngOnInit() {
  }

  ngAfterViewChecked() {
    this.filterPhotos();
    this.filterVideos();
  }

  filterPhotos() {
    $('#filterPhotos').click(function(e){
      e.preventDefault();
      $('.mediaroll-videos').hide();
      $('.mediaroll-pictures').show();
    });
  }

  filterVideos() {
    $('#filterVideos').click(function(e){
      e.preventDefault();
      $('.mediaroll-videos').show();
      $('.mediaroll-pictures').hide();
    });
  }

}
