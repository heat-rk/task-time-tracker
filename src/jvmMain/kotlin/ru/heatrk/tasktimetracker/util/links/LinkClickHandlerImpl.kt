package ru.heatrk.tasktimetracker.util.links

import java.awt.Desktop
import java.net.URI

class LinkClickHandlerImpl: LinkClickHandler {
    override fun onClick(url: String) {
        Desktop.getDesktop().browse(URI(url))
    }
}